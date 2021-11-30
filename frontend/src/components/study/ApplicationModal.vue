<template>
  <Modal @closeModal="handleCloseModal">
    <header class="flex items-center justify-between pb-4 border-b mb-6">
      <h4>지원하기</h4>
      <button class="flex material-icons" @click="handleCloseModal">
        close
      </button>
    </header>
    <div>
      <div class="form-field mb-6">
        <label for="bio">자기소개</label>
        <textarea v-model="bio" name="bio" id="bio" rows="6"></textarea>
      </div>
      <div class="buttons flex gap-4 w-full justify-center">
        <button
          class="rounded py-2 px-6 text-sm font-bold bg-blue-500 text-white"
          @click="handleSubmit"
        >
          확인
        </button>
      </div>
    </div>
  </Modal>
</template>

<script>
import { ref } from "vue"
import { useStore } from "vuex"
import Modal from "@/components/common/Modal.vue"

export default {
  name: "ApplicationModal",
  components: { Modal },
  props: {
    studyId: [String, Number],
  },
  emits: ["closeModal"],
  setup(props, { emit }) {
    const store = useStore()
    const bio = ref("")

    const handleSubmit = async () => {
      await store.dispatch("study/submitApplication", {
        bio: bio.value,
        studyId: props.studyId,
      })
    }

    const handleCloseModal = () => {
      emit("closeModal")
    }

    return {
      bio,
      handleSubmit,
      handleCloseModal,
    }
  },
}
</script>

<style></style>
