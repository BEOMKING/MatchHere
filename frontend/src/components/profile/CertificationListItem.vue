<template>
  <div class="card flex" :class="{ 'border-b': editMode, 'pb-4': editMode }">
    <div class="grid gap-2 flex-1">
      <div class="flex items-center gap-1 font-medium">
        <span class="text-lg">{{ certification.name }}</span>
        <span v-if="certification.grade" class="text-sm"
          >({{ certification.grade }})</span
        >
      </div>
      <p class="text-sm font-medium">{{ certification.code }}</p>
      <p class="font-medium">{{ certification.organization }}</p>
      <p class="text-sm text-gray-700">
        {{ certification.issued_date }} -
        {{ certification.expired_date || "만료기한 없음" }}
      </p>
      <p class="truncate" v-if="certification.description">
        {{ certification.description }}
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
    <CertificationFormModal
      v-if="modalOpen"
      :certificationId="certification.id"
      type="EDIT"
      @closeModal="handleToggleModal"
    />
  </div>
</template>

<script>
import CertificationFormModal from "@/components/profile/CertificationFormModal.vue"
import { ref } from "vue"
import { useStore } from "vuex"
export default {
  name: "CertificationListItem",
  components: { CertificationFormModal },
  props: {
    certification: {
      type: Object,
    },
    editMode: {
      type: Boolean,
      default: true,
    },
  },
  emits: ["updateCertification"],
  setup(props, { emit }) {
    const store = useStore()
    const modalOpen = ref(false)
    const handleToggleModal = ({ action, data }) => {
      modalOpen.value = !modalOpen.value
      store.commit("SET_MODAL_OPEN", modalOpen)
      if (action === "update") {
        emit("updateCertification", { action, data })
      }
    }

    const handleDelete = async () => {
      const ok = confirm("삭제하시겠습니까?")
      if (!ok) return
      await store.dispatch("member/deleteCertification", props.certification.id)
      emit("updateCertification", {
        action: "delete",
        data: { ...props.certification },
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
