<template>
  <div class="container max-w-xl py-10">
    <h2 class="mb-4">게시글 작성</h2>
    <BoardArticleForm @onSubmit="handleSubmit" :studyId="studyId" />
  </div>
</template>

<script>
import BoardArticleForm from "@/components/common/board/BoardArticleForm.vue"
import { useStore } from "vuex"
import { useRouter } from "vue-router"

export default {
  name: "StudyBoardArticleCreate",
  components: { BoardArticleForm },
  props: {
    studyId: [String, Number],
  },
  setup(props) {
    const store = useStore()
    const router = useRouter()
    const handleSubmit = async (data) => {
      console.log(data)
      const { studyBoardId } = data
      await store.dispatch("study/createBoardArticle", data)
      router.push({
        name: "StudyBoardArticleList",
        params: {
          studyId: props.studyId,
          boardId: studyBoardId,
        },
      })
    }

    return {
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
