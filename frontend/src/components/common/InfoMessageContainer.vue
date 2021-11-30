<template>
  <div class="message-container">
    <slot></slot>
  </div>
</template>

<script>
export default {
  name: "InfoContainer",
}
</script>

<style lang="scss" scoped>
.message-container {
  @apply w-full border border-blue-200 bg-blue-50 rounded px-4 py-6;
}
</style>
