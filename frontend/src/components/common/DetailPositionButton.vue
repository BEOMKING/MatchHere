<template>
  <button class="dp" :class="{ selected: dp.selected }" @click="handleClick">
    {{ dp.value }}
  </button>
</template>

<script>
export default {
  name: "detailPositionButton",
  props: {
    dp: {
      type: Object,
    },
  },
  emits: ["onUpdateStatus"],
  setup(props, { emit }) {
    const handleClick = () => {
      emit("onUpdateStatus", props.dp.id)
    }
    return {
      handleClick,
    }
  },
}
</script>

<style lang="scss" scoped>
.dp {
  @apply py-2 px-4 border-gray-400 rounded bg-gray-200 text-gray-600 text-sm font-medium transition-colors;

  &.selected {
    @apply bg-blue-500 border-0 text-white font-bold;
  }

  &:hover {
    @apply bg-blue-200;
  }
}
</style>
