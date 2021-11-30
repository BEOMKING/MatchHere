<template>
  <a
    class="link"
    v-if="portfolioUri"
    :href="portfolioUri"
    target="_blank"
    rel="noopener noreferrer"
  >
    <p class="flex-1">{{ portfolioUri }}</p>
    <span
      class="material-icons-outlined text-gray-700"
      style="font-size: 1.25rem"
    >
      open_in_new
    </span>
  </a>
</template>

<script>
export default {
  name: "PortfolioUri",
  props: {
    portfolioUri: String,
  },
}
</script>

<style lang="scss" scoped>
.link {
  @apply flex items-center p-2 transition-colors;

  &:hover {
    @apply bg-blue-50;
  }
}
</style>
