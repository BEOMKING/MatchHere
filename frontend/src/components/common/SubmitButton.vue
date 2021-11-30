<template>
  <button :class="{ disabled }" :disabled="disabled">
    <slot></slot>
  </button>
</template>

<script>
export default {
  name: "SubmitButton",
  props: {
    disabled: {
      type: Boolean,
      default: false,
    },
  },
}
</script>

<style lang="scss" scoped>
button {
  @apply w-full py-4 bg-blue-500 font-bold text-white rounded-xl;

  &.disabled {
    @apply bg-gray-400;
  }
}
</style>
