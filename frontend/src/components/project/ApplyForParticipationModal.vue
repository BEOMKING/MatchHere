<template>
  <div class="modal flex flex-col justify-center">
    <div class="overlay"></div>
    <div class="wrapper">
      <div class="m-8 shadow-xl rounded-md py-6 grid gap-6 bg-white">
        <section class="grid gap-4">
          <div class="flex justify-between mx-6">
            <h4 class="font-bold text-lg text-gray-700">지원하기</h4>
            <button>
              <span class="material-icons" @click="$emit('close')">close</span>
            </button>
          </div>
          <hr />
        </section>
        <section class="grid gap-4">
          <div class="mx-6 grid gap-2">
            <h5 class="font-medium text-sm">지원 포지션</h5>
            <select
              v-model="formFields.role"
              class="
                border-gray-400 border
                rounded-md
                outline-none
                py-2
                px-4
                font-medium
              "
              :class="{ 'text-gray-400': !formFields.role }"
            >
              <option disabled value="">개발자 or 기획자 or 디자이너</option>
              <option value="개발자">개발자</option>
              <option value="기획자">기획자</option>
              <option value="디자이너">디자이너</option>
            </select>
          </div>
          <div class="mx-6 grid gap-2">
            <h5 class="font-medium text-sm">자기소개</h5>
            <textarea
              class="rounded p-4 outline-none border-gray-200 border"
              style="height: 35vh"
              placeholder="프로젝트장에게 소개할 글을 작성하세요"
              v-model="formFields.bio"
            ></textarea>
          </div>
        </section>
        <section class="flex justify-center">
          <button
            class="submitButton"
            :class="{ 'bg-gray-400': !canSubmit, 'bg-blue-500': canSubmit }"
            :disabled="!canSubmit"
            @click="submit"
          >
            제출
          </button>
        </section>
      </div>
    </div>
  </div>
</template>

<script>
import { computed, ref } from "vue"
import { useStore } from "vuex"
export default {
  name: "ApplyForParticipationModal",
  emits: ["close"],
  props: {
    projectId: Number,
  },
  setup(props, { emit }) {
    const store = useStore()

    const formFields = ref({
      role: "",
      bio: "",
    })

    const canSubmit = computed(
      () => formFields.value.role && formFields.value.bio
    )

    const submit = async () => {
      try {
        await store.dispatch("project/applyForParticipation", {
          reqForm: formFields.value,
          projectId: props.projectId,
        })
        store.commit("ADD_MESSAGE", { text: "참가 신청 완료! 😎" })
      } catch (error) {
        store.commit("ADD_MESSAGE", {
          text: "이미 참가 신청을 했어요 😅",
          type: "warning",
        })
      }
      emit("close")
    }

    return { formFields, submit, canSubmit }
  },
}
</script>

<style lang="scss" scoped>
.modal {
  @apply w-screen h-screen fixed left-0 top-0;
  .overlay {
    @apply w-screen h-screen fixed left-0 top-0 opacity-50 bg-black;
  }
  .wrapper {
    @apply relative;

    .submitButton {
      @apply text-white rounded-lg px-6 py-2;
    }
  }
}
</style>
