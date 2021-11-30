<template>
  <li>
    <div class="left">
      <img :src="require('@/assets/images/noStack.png')" alt="로고" />
      <span>{{ name }}</span>
    </div>
    <div class="right">
      <div class="levels">
        <button
          v-for="level in levels"
          :key="level"
          :class="{ active: level === userLevel }"
          @click="handleClickLevelButton(level)"
        >
          {{ level }}
        </button>
      </div>
      <button class="close-button" @click="handleClickRemoveButton">
        <span class="material-icons">close</span>
      </button>
    </div>
  </li>
</template>

<script>
export default {
  name: "TeckStackListItem",
  props: {
    name: String,
    userLevel: String,
  },
  emits: ["change:userLevel", "remove:teckStack"],
  setup(props, { emit }) {
    const levels = ["하", "중", "상"]

    const handleClickLevelButton = (level) => {
      emit("change:userLevel", { key: props.name, level })
    }

    const handleClickRemoveButton = () => {
      emit("remove:teckStack", props.name)
    }

    return {
      levels,
      handleClickLevelButton,
      handleClickRemoveButton,
    }
  },
}
</script>

<style lang="scss" scoped>
li {
  @apply flex items-center justify-between p-2 transition-colors;

  &:hover {
    @apply bg-blue-50;
  }

  .left {
    @apply flex items-center justify-between gap-2;

    img {
      @apply w-6 h-6;
    }
  }

  .right {
    @apply flex items-center justify-between gap-4;

    .levels {
      @apply flex rounded overflow-hidden bg-gray-200;

      button {
        @apply text-sm py-1 px-2;

        &.active {
          @apply text-white bg-blue-500;
        }
      }
    }

    .close-button {
      @apply transition-colors flex items-center justify-center p-1;

      &:hover {
        @apply bg-red-50;
      }

      .material-icons {
        font-size: 1.25rem;
        @apply text-red-500;
      }
    }
  }
}
</style>
