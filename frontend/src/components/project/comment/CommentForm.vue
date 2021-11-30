<template>
  <div class="grid gap-2">
    <textarea placeholder="댓글을 작성하세요" v-model="content"></textarea>
    <span class="justify-self-end">
      <button
        @click="handleClick"
        :class="{ disabled: !content.trim() }"
        :disabled="!content.trim()"
      >
        작성
      </button>
    </span>
  </div>
</template>

<script>
import { ref } from "@vue/reactivity"
import { useStore } from "vuex"

export default {
  name: "CommentForm",
  props: {
    articleId: Number,
    parentId: {
      type: Number,
      default: 0,
    },
  },
  emits: ["create"],
  setup(props, { emit }) {
    const store = useStore()

    const content = ref("")

    const handleClick = async () => {
      const { articleId, parentId } = props
      try {
        const resData = await store.dispatch("project/createComment", {
          content: content.value,
          articleId,
          parentId,
        })
        console.log(resData)
        content.value = ""
        emit("create", resData)
      } catch (error) {
        console.log(error)
      }
    }
    return { handleClick, content }
  },
}
</script>

<style lang="scss" scoped>
textarea {
  @apply w-full h-20 border border-gray-200 rounded p-4;
}

button {
  @apply py-1 px-6 rounded bg-blue-500 text-white text-sm font-bold hover:bg-blue-400;
  &.disabled {
    @apply bg-gray-500;
  }
}
</style>
