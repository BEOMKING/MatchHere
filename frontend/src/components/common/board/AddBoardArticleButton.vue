<template>
  <button class="fixed right-4" style="bottom: 73px" @click="$emit('addClick')">
    <span
      class="material-icons rounded-full shadow-md bg-blue-500 text-white p-4"
    >
      add
    </span>
  </button>
</template>

<script>
export default {
  name: "AddBoardArticleButton",
  emits: ["addClick"],
}
</script>

<style lang="scss" scoped></style>
