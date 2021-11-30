<template>
  <div class="flex pb-4" :class="{ 'border-b': editMode }">
    <div class="grid gap-2 flex-1">
      <span class="text-lg font-medium">{{ education.institution }}</span>
      <div class="flex items-center gap-1 font-medium">
        <span class="text-sm font-medium">{{ education.major }}</span>
        <span v-if="education.degree" class="text-sm">
          ({{ education.degree }})
        </span>
      </div>
      <p class="text-sm text-gray-700">
        {{ education.start_date }}
        {{ education.end_date && `- ${education.end_date}` }} |
        {{ education.state }}
      </p>
      <p class="truncate" v-if="education.description">
        {{ education.description }}
      </p>
    </div>
    <div class="btns flex flex-col gap-2" v-if="editMode">
      <button @click="handleToggleModal">
        <span
          class="material-icons text-gray-300 hover:text-blue-500"
          style="font-size: 1.25rem"
        >
          edit
        </span>
      </button>
      <button @click="handleDelete">
        <span
          class="material-icons text-gray-300 hover:text-red-500"
          style="font-size: 1.25rem"
        >
          delete
        </span>
      </button>
    </div>
    <EduFormModal
      v-if="modalOpen"
      :educationId="education.id"
      type="EDIT"
      @closeModal="handleToggleModal"
    />
  </div>
</template>

<script>
import EduFormModal from "@/components/profile/EduFormModal.vue"
import { ref } from "vue"
import { useStore } from "vuex"
export default {
  name: "EduListItem",
  components: { EduFormModal },
  props: {
    education: {
      type: Object,
    },
    editMode: {
      type: Boolean,
      default: true,
    },
  },
  emits: ["updateEducation"],
  setup(props, { emit }) {
    const store = useStore()
    const modalOpen = ref(false)
    const handleToggleModal = ({ action, data }) => {
      modalOpen.value = !modalOpen.value
      store.commit("SET_MODAL_OPEN", modalOpen)
      if (action === "update") {
        emit("updateEducation", { action, data })
      }
    }

    const handleDelete = async () => {
      const ok = confirm("삭제하시겠습니까?")
      if (!ok) return
      await store.dispatch("member/deleteEdu", props.education.id)
      emit("updateEducation", {
        action: "delete",
        data: { ...props.education },
      })
    }

    return {
      modalOpen,
      handleToggleModal,
      handleDelete,
    }
  },
}
</script>

<style lang="scss" scoped></style>
