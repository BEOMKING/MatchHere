<template>
  <li :class="type">{{ message }}</li>
</template>

<script>
export default {
  name: "AlertMessage",
  props: {
    message: {
      type: String,
      default: "메세지입니다",
    },
    type: {
      type: String,
      default: "success",
    },
  },
}
</script>

<style lang="scss" scoped>
li {
  transition: all 0.8s;
  @apply p-4 shadow-md rounded select-none w-72 mb-4;

  &.success {
    @apply bg-blue-500 text-white font-bold;
  }

  &.error {
    @apply bg-red-400 text-white font-bold;
  }

  &.warning {
    @apply bg-yellow-400 text-gray-900 font-bold;
  }
}
</style>
