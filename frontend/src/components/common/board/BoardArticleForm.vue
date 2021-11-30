<template>
  <form @submit.prevent="handleSubmit" class="grid gap-10">
    <div class="grid gap-4">
      <div class="form-field">
        <label for="title">게시글 제목</label>
        <input type="text" id="title" v-model="fields.title.value" />
      </div>
      <div class="form-field">
        <label class="label">{{ fields.board.label }}</label>
        <div class="select-wrapper">
          <select v-model="fields.board.value">
            <option disabled value="">{{ fields.board.placeholder }}</option>
            <option
              v-for="item in fields.board.options"
              :key="item"
              :value="item.id"
            >
              {{ item.name }}
            </option>
          </select>
          <span class="material-icons icon">expand_more</span>
        </div>
      </div>
      <div class="form-field">
        <label for="content">내용</label>
        <textarea id="content" v-model="fields.content.value" />
      </div>
      <div class="form-field">
        <label for="tag">태그</label>
        <div class="input-wrapper">
          <input
            type="text"
            id="tag"
            v-model="fields.tag.value"
            @keydown.enter.prevent="handleAddTag"
          />
          <span
            class="material-icons icon cursor-pointer"
            @click="handleAddTag"
          >
            add
          </span>
        </div>
        <div class="tags">
          <Tag
            v-for="tag in fields.tag.selectedTags"
            @click="handleRemoveTag(tag)"
            :key="tag"
            :text="tag"
          />
        </div>
      </div>
    </div>
    <SubmitButton :disabled="!canSubmit">확인</SubmitButton>
  </form>
</template>

<script>
import { computed, reactive, ref } from "@vue/reactivity"
import Tag from "@/components/common/Tag.vue"
import SubmitButton from "@/components/common/SubmitButton.vue"
import { onMounted } from "@vue/runtime-core"
import { useStore } from "vuex"

export default {
  name: "BoardArticleForm",
  components: { Tag, SubmitButton },
  emits: ["onSubmit"],
  props: {
    studyId: [String, Number],
    article: Object,
  },
  setup(props, { emit }) {
    const store = useStore()
    const fields = reactive({
      title: {
        value: "",
      },
      board: {
        label: "게시판 선택",
        value: "",
        placeholder: "게시판을 선택하세요",
        options: [],
      },
      content: {
        value: "",
      },
      tag: {
        value: "",
        selectedTags: [],
      },
    })
    const boards = ref([])

    const canSubmit = computed(() => {
      return fields.title.value && fields.board.value && fields.content.value
    })

    const handleAddTag = () => {
      if (
        fields.tag.value &&
        fields.tag.selectedTags.indexOf(fields.tag.value) === -1
      ) {
        fields.tag.selectedTags.push(fields.tag.value)
      }
      fields.tag.value = ""
    }

    const handleRemoveTag = (selectedTag) => {
      fields.tag.selectedTags = fields.tag.selectedTags.filter(
        (tag) => tag !== selectedTag
      )
    }

    const handleSubmit = () => {
      const data = {
        content: fields.content.value,
        studyBoardId: fields.board.value,
        tags: [...fields.tag.selectedTags],
        title: fields.title.value,
      }
      emit("onSubmit", data)
    }

    onMounted(async () => {
      const data = await store.dispatch("study/getStudyBoards", props.studyId)
      fields.board.options = [...data]
      if (props.article) {
        const { title, studyBoard, content, tags } = props.article
        fields.title.value = title
        fields.board.value = fields.board.options.find(
          (b) => b.name === studyBoard
        ).id
        fields.content.value = content
        fields.tag.selectedTags = [...tags]
        console.log(fields)
      }
    })

    return {
      fields,
      boards,
      canSubmit,
      handleAddTag,
      handleRemoveTag,
      handleSubmit,
    }
  },
}
</script>

<style lang="scss" scoped>
.tags {
  @apply flex flex-wrap gap-2;
}
</style>
