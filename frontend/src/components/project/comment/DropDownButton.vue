<template>
  <button class="comment-button" @click="$emit('btnClick')">
    <span v-if="isDropDownActive" class="material-icons">
      arrow_drop_down
    </span>
    <span v-else class="material-icons"> arrow_drop_up </span>
    <span class="comment-text" v-if="cntNestedComment"
      >{{ cntNestedComment }}개의 답글</span
    >
    <span v-else class="comment-text">답글</span>
  </button>
</template>

<script>
export default {
  name: "DropDownButton",
  props: ["isDropDownActive", "cntNestedComment"],
  edmits: ["btnClick"],
  setup() {},
}
</script>

<style lang="scss" scoped>
.comment-button {
  @apply flex gap-1 items-center;

  .material-icons {
    @apply text-blue-600 text-lg;
  }

  .comment-text {
    @apply text-blue-600 font-medium text-sm;
  }
}
</style>
