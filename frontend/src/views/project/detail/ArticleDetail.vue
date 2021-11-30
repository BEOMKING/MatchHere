<template>
  <div class="py-6 px-4 grid gap-6" v-if="article">
    <div>
      <div class="grid gap-4">
        <h3 class="font-bold text-xl text-gray-900">
          {{ article.title }}
        </h3>
        <div class="grid gap-2">
          <div class="flex gap-2">
            <span
              v-for="tag in article.tags"
              :key="tag"
              class="rounded bg-gray-100 text-gray-600 font-bold text-sm"
              style="padding: 2px 8px"
              >{{ tag }}</span
            >
          </div>
          <p class="flex gap-2 items-center">
            <img :src="profilePic" alt="" class="w-6 h-6 rounded-full" />
            <span class="text-xs text-gray-600">{{
              article.createdMember
            }}</span>
            <span class="text-xs text-gray-500">{{
              article.createdDate.slice(0, 10)
            }}</span>
          </p>
        </div>
        <hr />
        <div
          v-html="article.content.replace(/(?:\r\n|\r|\n)/g, '<br />')"
        ></div>
      </div>
    </div>
    <div class="pt-4 grid gap-4">
      <hr />
      <div class="grid gap-2">
        <h4 class="font-medium text-lg text-gray-900">
          {{ commentList.length }}개의 댓글
        </h4>
        <CommentForm
          :articleId="article.articleId"
          @create="handleCreateComment"
        />
      </div>
    </div>
    <div class="grid gap-6" v-if="commentList.length !== 0">
      <BoardCommentItem
        v-for="commentItem in commentList"
        :key="commentItem[0].id"
        :parentComment="commentItem[0]"
        :nestedCommentList="commentItem.slice(1)"
        :articleId="article.articleId"
      />
    </div>
  </div>
</template>

<script>
import { ref } from "@vue/reactivity"
import CommentForm from "@/components/project/comment/CommentForm.vue"
import BoardCommentItem from "@/components/project/comment/BoardCommentItem.vue"
import { useStore } from "vuex"
import { useRoute } from "vue-router"
import { onMounted } from "@vue/runtime-core"

export default {
  name: "BoardDetail",
  components: { BoardCommentItem, CommentForm },
  setup() {
    const route = useRoute()
    const store = useStore()

    const article = ref()
    const commentList = ref([])
    onMounted(async () => {
      const { articleId } = route.params
      try {
        const resArticle = await store.dispatch(
          "project/getBoardArticleDetail",
          articleId
        )
        console.log(resArticle)
        article.value = resArticle
        const resCommentList = await store.dispatch(
          "project/getArticleComment",
          articleId
        )
        console.log(resCommentList)
        if (resCommentList.length) {
          let parentId = 0
          let commentItem = []
          for (let comment of resCommentList) {
            if (parentId !== comment.parentId) {
              parentId = comment.parentId
              if (commentItem.length) {
                commentList.value.push(commentItem)
                commentItem = []
              }
            }
            commentItem.push(comment)
          }
          commentList.value.push(commentItem)
        }
      } catch (error) {
        console.log(error.message)
      }
    })

    const handleCreateComment = (comment) => {
      console.log(comment)
      commentList.value.push([comment])
    }

    const profilePic = ref(require("@/assets/images/test-profile.png"))

    return { profilePic, article, commentList, handleCreateComment }
  },
}
</script>

<style lang="scss" scoped></style>
