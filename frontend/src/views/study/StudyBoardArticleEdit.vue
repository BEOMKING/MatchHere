<template>
  <div class="container py-10">
    <h2 class="mb-4">게시글 수정</h2>
    <StudyBoardForm
      v-if="!loading"
      :studyId="studyId"
      :article="article"
      @onSubmit="handleSubmit"
    />
  </div>
</template>

<script>
import StudyBoardForm from "@/components/common/board/BoardArticleForm.vue"
import { useStore } from "vuex"
import { onMounted, ref } from "@vue/runtime-core"
import { useRouter } from "vue-router"

export default {
  name: "StudyBoardArticleEdit",
  components: { StudyBoardForm },
  props: {
    studyId: [String, Number],
    articleId: [String, Number],
  },
  setup(props) {
    const store = useStore()
    const router = useRouter()
    const loading = ref(true)
    const article = ref(null)

    const handleSubmit = async (articleForm) => {
      const articleData = await store.dispatch("study/updateBoardArticle", {
        articleId: props.articleId,
        article: articleForm,
      })
      console.log(articleData)
      router.push({
        name: "StudyBoardArticleDetail",
        params: { articleId: props.articleId },
      })
    }

    onMounted(async () => {
      const articleData = await store.dispatch(
        "study/getBoardArticle",
        props.articleId
      )
      article.value = articleData
      loading.value = false
    })

    return {
      loading,
      article,
      handleSubmit,
    }
  },
}
</script>

<style lang="scss" scoped>
.tags {
  @apply flex gap-2 flex-wrap;
}
</style>
