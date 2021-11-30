<template>
  <router-view @routeChat="handleRoute" />
</template>

<script>
export default {
  name: "ChatLayout",
  emits: ["routeChatDetail", "routeChatList"],
  setup(props, { emit }) {
    const handleRoute = (child) => {
      if (child === "detail") emit("routeChatDetail")
      else if (child === "list") emit("routeChatList")
    }
    return { handleRoute }
  },
}
</script>
<style lang="scss" scoped></style>
