<template>
  <div class="py-10 px-4 grid gap-10">
    <div class="grid gap-4">
      <h3 class="font-bold text-xl text-gray-900">게시글 작성</h3>
      <InputFormField :field="title" v-model="title.value" />
      <SelectFormField :field="boardChoice" v-model="boardChoice.value" />
      <TextAreaFormField :field="content" v-model="content.value" />
      <InputFormField :field="tags" v-model="tags.value" />
    </div>
    <button
      class="rounded-xl text-white font-bold py-4"
      :class="{ 'bg-gray-400': !canSubmit, 'bg-blue-500': canSubmit }"
      :disabled="!canSubmit"
      @click="handleSubmit"
    >
      확인
    </button>
  </div>
</template>

<script>
import { InputFormFieldMaker } from "@/libs/func"
import InputFormField from "@/components/common/formField/InputFormField.vue"
import SelectFormField from "@/components/common/formField/SelectFormField.vue"
import TextAreaFormField from "@/components/common/TextAreaFormField.vue"
import { computed, ref } from "@vue/reactivity"
import { onMounted } from "@vue/runtime-core"
import { useStore } from "vuex"
import { useRoute, useRouter } from "vue-router"

export default {
  name: "ArticleForm",
  components: { InputFormField, SelectFormField, TextAreaFormField },
  setup() {
    const route = useRoute()
    const store = useStore()
    const router = useRouter()

    const boardList = ref()
    const projectId = ref()
    onMounted(async () => {
      projectId.value = route.params.projectId
      const resData = await store.dispatch(
        "project/getBoardList",
        projectId.value
      )
      boardList.value = resData
    })

    const title = ref(new InputFormFieldMaker("title"))
    const boardChoice = ref({
      label: "게시판 선택",
      value: "",
      placeholder: "게시판을 선택하세요",
      options: ["공지사항", "게시판"],
    })
    const content = ref({
      label: "내용",
      value: "",
      key: "content",
    })
    const tags = ref(new InputFormFieldMaker("tags"))

    const canSubmit = computed(() => {
      return Boolean(
        title.value.value && boardChoice.value.value && content.value.value
      )
    })

    const handleSubmit = async () => {
      const board = boardList.value.find(
        (board) => board.name === boardChoice.value.value
      )
      let tagList = tags.value.value.split(",")
      tagList = tagList.map((tag) => tag.trim())
      tagList = tagList.filter((tag) => tag !== "")

      const reqForm = {
        content: content.value.value,
        projectBoardId: board.id,
        tags: tagList,
        title: title.value.value,
      }
      try {
        const resData = await store.dispatch(
          "project/createBoardArticle",
          reqForm
        )
        console.log(resData)

        let name
        let boardId
        if (board.name === "공지사항") {
          name = "NotiBoardArticleList"
          boardId = boardList.value[0].id
        } else if (board.name === "게시판") {
          name = "BoardArticleList"
          boardId = boardList.value[1].id
        }
        store.commit("ADD_MESSAGE", {
          text: "게시글을 생성했습니다! 😊",
        })
        router.push({ name, params: { projectId: projectId.value, boardId } })
      } catch (error) {
        console.log(error)
        store.commit("ADD_MESSAGE", {
          text: "게시글 생성에 실패했어요 😢",
          type: "error",
        })
      }
    }

    return { title, boardChoice, tags, content, canSubmit, handleSubmit }
  },
}
</script>

<style lang="scss" scoped></style>
