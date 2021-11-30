<template>
  <div class="comment" :class="{ nested: nestedComment }">
    <div class="info-wrapper">
      <div class="img-wrapper">
        <img
          v-if="comment.writer.coverPicUri"
          :src="comment.writer.coverPicUri"
          alt="프로필 이미지"
        />
      </div>
      <div class="info-text-wrapper">
        <p class="name">{{ comment.writer.name }}</p>
        <p class="date">{{ formattedCreateDate }}</p>
      </div>
    </div>
    <p class="content">{{ comment.content }}</p>
    <button
      class="delete-btn"
      @click="handleDelete"
      v-if="comment.writer.id === userId"
    >
      삭제
    </button>
  </div>
</template>

<script>
import { dateFormatter } from "@/libs/func"
import { computed } from "@vue/reactivity"
import { useStore } from "vuex"
export default {
  name: "Comment",
  props: {
    comment: Object,
    nestedComment: {
      type: Boolean,
      default: false,
    },
  },
  emits: ["onDelete"],
  setup(props, { emit }) {
    const store = useStore()
    const userId = computed(() => store.state.member.user?.id)
    const formattedCreateDate = computed(() =>
      dateFormatter(props.comment.createDate)
    )

    const handleDelete = async () => {
      await store.dispatch("study/deleteArticleComment", props.comment.id)
      emit("onDelete", props.comment)
    }

    return { userId, formattedCreateDate, handleDelete }
  },
}
</script>

<style lang="scss" scoped>
.comment {
  @apply relative py-4 grid gap-4;

  &.nested {
    @apply pb-6 border-b border-gray-200;
  }

  .info-wrapper {
    @apply flex gap-4;

    .img-wrapper {
      @apply w-12 h-12 bg-pink-200 border rounded-full overflow-hidden;

      img {
        @apply w-full h-full object-cover;
      }
    }

    .info-text-wrapper {
      @apply grid gap-1;

      .name {
        @apply font-medium;
      }

      .date {
        @apply text-sm text-gray-500;
      }
    }
  }

  .delete-btn {
    @apply absolute top-4 right-2 text-sm text-red-500;
  }
}
</style>
