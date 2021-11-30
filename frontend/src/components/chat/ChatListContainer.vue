<template>
  <div
    class="overflow-y-scroll flex-1 w-full"
    @scroll="handleScroll"
    ref="scrollBarDiv"
  >
    <div class="flex flex-col gap-1 px-4 pb-4 w-full" ref="chatListDiv">
      <ChatItem v-for="(chatItem,idx) in chatList" :key="chatItem.sentTime"
      props: :myId="myId" :chatItem="chatItem" :exChatItem="idx !== 0 &&
      chatList[idx-1]" />
    </div>
  </div>
</template>

<script>
import ChatItem from "@/components/chat/ChatItem.vue"
import { watch, onUpdated, ref } from "@vue/runtime-core"

export default {
  name: "ChatListContainer",
  components: { ChatItem },
  // props: ["chatList", "myId"],
  props: {
    chatList: Object,
    myId: String,
  },
  emits: ["handleScroll"],
  setup(props, { emit }) {
    const scrollBarDiv = ref(null)
    const chatListDiv = ref(null)

    const oldScrollHeight = ref()
    const handleScroll = (e) => {
      if (e.target.scrollTop === 0) {
        oldScrollHeight.value = e.target.scrollHeight
        emit("handleScroll")
      }
    }

    let isFirstHistory = false
    let isAddedNewMsg = false
    let isAddedHistory = false
    watch(
      () => props.chatList,
      (newChatList, oldChatList) => {
        if (oldChatList.length === 0) isFirstHistory = true
        else if (newChatList[0].sentTime === oldChatList[0].sentTime)
          isAddedNewMsg = true
        else isAddedHistory = true
      }
    )

    onUpdated(() => {
      // chatListDiv.value.scrollIntoView({
      //   behavior: "auto", // "smooth", "auto"(default)
      //   block: "end", // "start", "center", "end", "nearest"(default)
      //   inline: "nearest", // "start", "center", "end", "nearest"(default)
      // })
      if (isFirstHistory) {
        chatListDiv.value.scrollIntoView(false)
        isFirstHistory = false
      } else if (isAddedNewMsg) {
        chatListDiv.value.scrollIntoView(false)
        isAddedNewMsg = false
      } else if (isAddedHistory) {
        scrollBarDiv.value.scrollTop =
          scrollBarDiv.value.scrollHeight - oldScrollHeight.value
        isAddedHistory = false
      }
    })

    return { chatListDiv, handleScroll, scrollBarDiv }
  },
}
</script>

<style lang="scss" scoped></style>
