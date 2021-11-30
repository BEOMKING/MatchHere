<template>
  <ChatUserListItem
    v-for="chatRoom in chatRoomList"
    :key="chatRoom.id"
    :chatRoom="chatRoom"
  />
</template>

<script>
import ChatUserListItem from "@/components/chat/ChatUserListItem.vue"
import { onMounted, ref } from "@vue/runtime-core"
import { useStore } from "vuex"

export default {
  name: "ChatList",
  components: { ChatUserListItem },
  emits: ["routeChat"],
  setup(props, { emit }) {
    emit("routeChat", "list")
    const store = useStore()

    const chatRoomList = ref()

    onMounted(async () => {
      try {
        const resData = await store.dispatch("chat/getChatList")
        // console.log(resData)
        chatRoomList.value = resData.filter((room) => {
          const userIds = room.id.split("-")
          return userIds[0] !== userIds[1]
        })
        console.log(chatRoomList.value)
      } catch (error) {
        console.log(error.message)
      }
    })

    return {
      chatRoomList,
    }
  },
}
</script>
<style lang="scss" scoped></style>
