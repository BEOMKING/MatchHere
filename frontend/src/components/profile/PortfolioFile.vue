<template>
  <a
    class="file flex cursor-pointer"
    v-if="portfolio.id"
    :href="portfolio.download_uri"
    target="_blank"
    rel="noopener noreferrer"
  >
    <p class="flex-1">{{ portfolio.file_name }}</p>
    <span
      class="material-icons-outlined text-gray-700"
      style="font-size: 1.25rem"
    >
      file_download
    </span>
  </a>
</template>

<script>
export default {
  name: "PortfolioFile",
  props: {
    portfolio: Object,
  },
}
</script>

<style></style>
