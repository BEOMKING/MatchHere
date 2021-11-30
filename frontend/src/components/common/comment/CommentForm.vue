<template>
  <form @submit.prevent="handleSubmit" class="grid gap-2 mb-6">
    <textarea
      class="border rounded w-full h-24 p-4 resize-none"
      name="comment"
      v-model="content"
    ></textarea>
    <button class="comment-submit-btn">작성</button>
  </form>
</template>

<script>
import { ref } from "vue"
export default {
  name: "CommentForm",
  emits: ["onSubmit"],
  setup(props, { emit }) {
    const content = ref("")

    const handleSubmit = () => {
      if (content.value) {
        emit("onSubmit", content.value)
      }
      content.value = ""
    }

    return {
      content,
      handleSubmit,
    }
  },
}
</script>

<style lang="scss" scoped>
.comment-submit-btn {
  @apply flex py-2 px-6 bg-blue-500 text-white font-bold justify-self-end rounded;
}
</style>
