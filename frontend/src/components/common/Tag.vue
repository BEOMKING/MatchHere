<template>
  <div
    class="tag"
    :class="{
      green: type === 'green',
      red: type === 'red',
      blue: type === 'blue',
      board: type === 'board',
    }"
  >
    {{ text }}
  </div>
</template>

<script>
export default {
  name: "Tag",
  props: {
    text: String,
    type: {
      type: String,
      default: "green",
    },
  },
}
</script>

<style lang="scss" scoped>
.tag {
  @apply py-1 px-2 text-sm font-bold rounded cursor-pointer;

  &.red {
    @apply bg-pink-100 text-pink-600;
  }
  &.blue {
    @apply bg-blue-100 text-blue-600;
  }
  &.green {
    @apply bg-green-100 text-green-600;
  }

  &.board {
    @apply py-0.5 px-2 bg-gray-100 text-gray-600;
  }
}
</style>
