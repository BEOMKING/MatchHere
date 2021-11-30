<template>
  <div class="card flex" :class="{ 'border-b': editMode, 'pb-4': editMode }">
    <div class="grid gap-2 flex-1">
      <p class="text-lg font-medium">{{ career.role }}</p>
      <p class="font-medium">{{ career.company }}</p>
      <p class="text-sm text-gray-700">
        {{ career.start_date }} - {{ career.end_date || "재직 중" }}
      </p>
      <p class="truncate">
        {{ career.description }}
      </p>
    </div>
    <div class="btns flex flex-col gap-1" v-if="editMode">
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
    <CareerFormModal
      v-if="careerModalOpen"
      :careerId="career.id"
      type="EDIT"
      @closeModal="handleToggleModal"
    />
  </div>
</template>

<script>
import CareerFormModal from "@/components/profile/CareerFormModal.vue"
import { ref } from "vue"
import { useStore } from "vuex"
export default {
  name: "CareerListItem",
  components: { CareerFormModal },
  props: {
    career: {
      type: Object,
    },
    editMode: {
      type: Boolean,
      default: true,
    },
  },
  emits: ["updateCareer"],
  setup(props, { emit }) {
    const store = useStore()
    const careerModalOpen = ref(false)

    const handleToggleModal = ({ action, data }) => {
      careerModalOpen.value = !careerModalOpen.value
      store.commit("SET_MODAL_OPEN", careerModalOpen)
      if (action === "update") {
        emit("updateCareer", { action, data })
      }
    }

    const handleDelete = async () => {
      const ok = confirm("삭제하시겠습니까?")
      if (!ok) return
      await store.dispatch("member/deleteCareer", props.career.id)
      emit("updateCareer", {
        action: "delete",
        data: { ...props.career },
      })
    }

    return {
      careerModalOpen,
      handleToggleModal,
      handleDelete,
    }
  },
}
</script>

<style lang="scss" scoped></style>
