<template>
  <div class="p-2">
    <div class="wrapper">
      <textarea
        :style="{ height: textareaHeight }"
        placeholder="메세지를 입력하세요"
        @keypress="resize"
        @keydown.enter="handleEnter"
        v-model="content"
      ></textarea>
      <span
        class="material-icons text-gray-400"
        :class="{ 'text-gray-600': content }"
        @click="handleClick"
      >
        send
      </span>
    </div>
  </div>
</template>

<script>
import { computed, ref } from "@vue/reactivity"
import { watch } from "@vue/runtime-core"
import { useStore } from "vuex"
export default {
  name: "ChatControler",
  setup() {
    const store = useStore()

    const textareaHeight = ref("24px")
    const resize = (e) => {
      const scrollHeight = e.target.scrollHeight
      if (scrollHeight > 48) textareaHeight.value = "72px"
      else if (scrollHeight > 24) textareaHeight.value = "48px"
    }

    const content = ref("")

    watch(content, () => {
      if (content.value === "") textareaHeight.value = "24px"
    })

    const stompClient = computed(() => store.getters["chat/getStompClient"])
    const targetUserId = computed(() => store.getters["chat/getTargetUserId"])

    const send = (content) => {
      const token = JSON.parse(localStorage.getItem("token")).accessToken
      const header = { Authorization: token }
      const msg = { content }

      stompClient.value.send(
        `/message/user/${targetUserId.value}`,
        header,
        JSON.stringify(msg)
      )
    }

    const handleEnter = (e) => {
      // just Enter
      if (!e.shiftKey) {
        e.preventDefault()
        if (content.value.trim()) {
          // console.log(content.value)
          send(content.value)
        }
        content.value = ""
        // shift + Enter
      } else {
        const scrollHeight = e.target.scrollHeight
        if (scrollHeight === 24) textareaHeight.value = "48px"
        else if (scrollHeight === 48) textareaHeight.value = "72px"
      }
    }
    const handleClick = () => {
      if (content.value.trim()) send(content.value)
      content.value = ""
    }

    return { resize, textareaHeight, content, handleEnter, handleClick }
  },
}
</script>

<style lang="scss" scoped>
.wrapper {
  @apply bg-gray-100 rounded-lg flex items-center gap-2 p-2;

  textarea {
    @apply flex-grow outline-none resize-none bg-gray-100;
  }
}
</style>
