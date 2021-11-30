<template>
  <nav>
    <div class="btns">
      <button
        :class="{ active: activeSectionId === 1 }"
        @click="handleClick(1)"
      >
        기본정보
      </button>
      <button
        :class="{ active: activeSectionId === 2 }"
        @click="handleClick(2)"
      >
        포지션/기술스택
      </button>
      <button
        :class="{ active: activeSectionId === 3 }"
        @click="handleClick(3)"
      >
        커리어
      </button>
      <button
        :class="{ active: activeSectionId === 4 }"
        @click="handleClick(4)"
      >
        포트폴리오/SNS
      </button>
    </div>
  </nav>
</template>

<script>
export default {
  name: "EditProfile",
  props: {
    activeSectionId: Number,
  },
  emits: ["update:activeSectionId"],
  setup(props, { emit }) {
    const handleClick = (id) => {
      if (id === props.activeSectionId) return
      emit("update:activeSectionId", id)
    }
    return {
      handleClick,
    }
  },
}
</script>

<style lang="scss" scoped>
nav {
  -ms-overflow-style: none; /* IE and Edge */
  scrollbar-width: none; /* Firefox */
  @apply border-b border-gray-200 overflow-auto w-screen -mx-4 mb-6 sm:w-full sm:mx-0;

  &::-webkit-scrollbar {
    display: none;
  }

  .btns {
    @apply flex flex-nowrap items-center;

    button {
      @apply flex-shrink-0 p-4 font-medium text-gray-400;

      &.active {
        @apply text-gray-900 font-bold;
      }
    }
  }
}
</style>
