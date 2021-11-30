<template>
  <teleport to="body">
    <div class="modal">
      <div class="background" @click.self="handleCloseModal"></div>
      <div class="inner-container">
        <slot></slot>
      </div>
    </div>
  </teleport>
</template>

<script>
export default {
  name: "Modal",
  emits: ["closeModal"],
  setup(_, { emit }) {
    const handleCloseModal = () => {
      emit("closeModal")
    }
    return {
      handleCloseModal,
    }
  },
}
</script>

<style lang="scss" scoped>
.modal {
  @apply fixed inset-0 z-30 py-6 px-4 flex items-center justify-center;
  .background {
    background-color: rgba(0, 0, 0, 0.4);
    @apply absolute inset-0;
  }

  .inner-container {
    @apply relative z-10 p-6 bg-white rounded-md w-full max-w-lg max-h-full overflow-auto;
  }
}
</style>
