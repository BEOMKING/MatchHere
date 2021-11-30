<template>
  <div class="flex flex-col h-screen w-screen max-w-screen">
    <ChatHeader :targetNickname="targetNickname" />
    <ChatListContainer
      :chatList="chatList"
      :myId="myId"
      @handleScroll="handleChatListContainerScroll"
    />
    <hr />
    <ChatControler />
  </div>
</template>

<script>
import SockJS from "sockjs-client"
import Stomp from "stompjs"
import ChatHeader from "@/components/chat/ChatHeader.vue"
import ChatListContainer from "@/components/chat/ChatListContainer.vue"
import ChatControler from "@/components/chat/ChatControler.vue"
import { ref } from "@vue/reactivity"
import { onBeforeUnmount, onMounted } from "@vue/runtime-core"
import { useRoute, useRouter } from "vue-router"
import { useStore } from "vuex"
export default {
  name: "ChatDetail",
  components: { ChatHeader, ChatListContainer, ChatControler },
  emits: ["routeChat"],
  setup(props, { emit }) {
    const route = useRoute()
    const router = useRouter()
    const store = useStore()
    emit("routeChat", "detail")

    const roomId = ref()
    const targetNickname = ref()
    const targetUserId = ref()
    const chatList = ref([])
    const myId = ref()
    const currentPage = ref(0)
    const lastPage = ref()
    const sizePerPage = 20

    onMounted(async () => {
      const { email } = route.params
      const resData = await store.dispatch("chat/getChatRoomInfo", email)
      roomId.value = resData.room_id
      targetUserId.value = resData.user_id
      targetNickname.value = resData.user_nickname
      // console.log(resData)

      const userIds = roomId.value.split("-")
      if (userIds[0] === userIds[1]) {
        router.push({ name: "ChatList" })
      } else {
        myId.value = userIds.filter(
          (id) => Number(id) !== targetUserId.value
        )[0]
        // get 채팅 히스토리
        getHistory()
        // 웹소켓 연결
        connection()
      }
    })

    onBeforeUnmount(() => disconnection())

    const handleChatListContainerScroll = () => {
      if (currentPage.value <= lastPage.value) getHistory()
    }

    const getHistory = async () => {
      const chatHistory = await store.dispatch("chat/getChatHistory", {
        roomId: roomId.value,
        pageNumber: currentPage.value++,
        size: sizePerPage,
      })
      // console.log(currentPage.value)
      if (chatHistory) {
        // console.log(chatHistory)
        lastPage.value = chatHistory.totalPages - 1
        chatList.value = [...chatHistory.content.reverse(), ...chatList.value]
        // console.log(chatList.value)
      }
    }

    const stompClient = ref(null)
    const connection = async () => {
      const socket = new SockJS("http://127.0.0.1:8080/api/chat")
      stompClient.value = Stomp.over(socket)

      const token = JSON.parse(localStorage.getItem("token")).accessToken
      const headers = { Authorization: token }

      // 연결
      await stompClient.value.connect(
        headers,
        (frame) => {
          console.log("소켓 연결 성공", frame)
          // 구독
          stompClient.value.subscribe(`/room/${roomId.value}`, (msg) => {
            console.log("메세지 도착")
            // chatList.value.push(JSON.parse(msg.body))
            chatList.value = [...chatList.value, JSON.parse(msg.body)]
            // console.log(chatList.value)
          })
        },
        (err) => console.log(err)
      )

      store.commit("chat/SET_CHAT_MATERIALS", {
        stompClient: stompClient.value,
        targetUserId: targetUserId.value,
      })
    }

    const disconnection = () => {
      if (stompClient.value !== null) {
        stompClient.value.disconnect()
        console.log("Disconnected!")
      }
    }

    return {
      chatList,
      myId,
      currentPage,
      handleChatListContainerScroll,
      targetNickname,
    }
  },
}
</script>
<style lang="scss" scoped></style>
