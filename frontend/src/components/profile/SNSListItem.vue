<template>
  <a
    class="link"
    :href="sns.snsAccount"
    target="_blank"
    rel="noopener noreferrer"
  >
    <div class="flex-1 grid gap-2">
      <p>{{ sns.snsAccount }}</p>
      <p class="text-sm text-gray-700">{{ sns.snsName }}</p>
    </div>
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
  name: "SNSListItem",
  props: {
    sns: Object,
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
