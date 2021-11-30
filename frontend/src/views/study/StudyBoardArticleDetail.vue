<template>
  <div class="container py-6">
    <article class="grid gap-4 mb-4" v-if="!loading">
      <header class="pb-4 border-b">
        <h3 class="mb-4">{{ article.title }}</h3>
        <div class="tags flex gap-2 mb-2">
          <Tag
            v-for="tag in article.tags"
            :key="tag"
            :text="tag"
            type="board"
          />
        </div>
        <div class="flex gap-2 items-center justify-between">
          <Member :user="article.writer" />
          <span class="text-sm">{{ article.createdDate }}</span>
        </div>
      </header>
      <div class="pb-10 border-b">
        <p>
          {{ article.content }}
        </p>
      </div>
    </article>
    <section>
      <h4 class="font-medium mb-2">{{ commentList.length }}개의 댓글</h4>
      <CommentForm @onSubmit="handleCreateComment" />
      <div class="comment-list">
        <CommentListItem
          v-for="comment in commentList"
          :key="comment"
          :comment="comment"
          :articleId="articleId"
          @onSubmitComment="handleSubmitReComment"
          @onDelete="handleDeleteComment"
        />
      </div>
    </section>
  </div>
</template>

<script>
import Tag from "@/components/common/Tag.vue"
import Member from "@/components/common/Member.vue"
import CommentListItem from "@/components/common/comment/CommentListItem.vue"
import CommentForm from "@/components/common/comment/CommentForm.vue"
import { useStore } from "vuex"
import { onMounted, ref } from "@vue/runtime-core"
import { dateFormatter } from "@/libs/func"

export default {
  name: "StudyBoardArticleDetail",
  components: {
    Tag,
    Member,
    CommentListItem,
    CommentForm,
  },
  props: {
    studyId: [String, Number],
    boardId: [String, Number],
    articleId: [String, Number],
  },
  setup(props) {
    const store = useStore()
    const loading = ref(true)
    const article = ref(null)
    const commentList = ref([])

    const handleSubmitReComment = async ({ newComment, parentId }) => {
      // TODO: 사용자 경험이 좋지 않다.
      // 펼쳐놨던 ReComment가 다시 접힘상태가 됨
      console.log(newComment, parentId)
      handleGetComments()
      // commentList.value.forEach((c) => {
      //   if (c.id === parentId) {
      //     c.reCommentList.push(newComment)
      //   }
      // })
    }

    const handleCreateComment = async (content) => {
      console.log(content)
      const payload = {
        articleId: props.articleId,
        content,
      }
      const newComment = await store.dispatch(
        "study/createArticleComment",
        payload
      )
      console.log(newComment)
      handleGetComments()
      // commentList.value.push({ ...newComment, reCommentList: [] })
    }

    const handleDeleteComment = async (targetComment) => {
      console.log(targetComment)
      handleGetComments()
      // if (targetComment.id === targetComment.parentId) {
      //   commentList.value = commentList.value.filter(
      //     (c) => c.id !== targetComment.id
      //   )
      // } else {
      //   commentList.value.forEach((c) => {
      //     if (c.id === targetComment.parentId) {
      //       c.reCommentList = c.reCommentList.filter(
      //         (reComment) => reComment.id !== targetComment.id
      //       )
      //       c.replyCount -= 1
      //     }
      //   })
      // }
    }

    const handleGetComments = async () => {
      const commentData = await store.dispatch(
        "study/getArticleComment",
        props.articleId
      )
      commentList.value = [...commentData]
    }

    onMounted(async () => {
      const data = await store.dispatch(
        "study/getBoardArticle",
        props.articleId
      )
      console.log(data)
      article.value = { ...data }
      article.value.createdDate = dateFormatter(article.value.createdDate)
      await handleGetComments()
      loading.value = false
    })
    return {
      loading,
      article,
      commentList,
      handleCreateComment,
      handleDeleteComment,
      handleSubmitReComment,
    }
  },
}
</script>

<style lang="scss" scoped>
.comment-submit-btn {
  @apply flex py-2 px-6 bg-blue-500 text-white font-bold justify-self-end rounded;
}
</style>
