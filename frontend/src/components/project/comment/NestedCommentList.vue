<template>
  <div class="wrapper">
    <div
      class="comment-wrapper"
      v-for="comment in commentList"
      :key="comment.id"
    >
      <Comment :comment="comment" />
      <hr />
    </div>
    <CommentForm
      :articleId="articleId"
      :parentId="parentId"
      @create="handleCreateComment"
    />
  </div>
</template>

<script>
import { ref } from "@vue/reactivity"
import Comment from "@/components/project/comment/Comment.vue"
import CommentForm from "@/components/project/comment/CommentForm.vue"

export default {
  name: "NestedCommentList",
  components: { Comment, CommentForm },
  props: ["nestedCommentList", "articleId", "parentId"],
  emits: ["create"],
  setup(props, { emit }) {
    const profilePic = ref(require("@/assets/images/test-profile.png"))

    const commentList = ref(props.nestedCommentList)

    const handleCreateComment = (comment) => {
      console.log(comment)
      commentList.value.push(comment)
      emit("create")
    }
    return { profilePic, commentList, handleCreateComment }
  },
}
</script>

<style lang="scss" scoped>
.wrapper {
  @apply grid gap-2 border border-gray-200 rounded bg-gray-50 p-4;

  .comment-wrapper {
    @apply grid gap-2;
  }
}
</style>
