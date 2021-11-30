<template>
  <div>
    <div v-if="isNewDay" class="text-center mt-4 text-gray-500 text-sm">
      {{ newChatDate }}
    </div>
    <div class="flex flex-col gap-1 w-full">
      <div
        v-if="!isContinue"
        class="mt-4 flex gap-2"
        :class="{ 'flex-row-reverse': isMe }"
      >
        <img
          :src="chatItem.pic_uri || require('@/assets/images/test-profile.png')"
          alt="사진"
          class="rounded-full w-10 h-10"
        />
        <div class="flex flex-col" :class="{ 'items-end': isMe }">
          <p class="font-medium text-sm text-gray-900">
            {{ chatItem.nickname }}
          </p>
          <p class="text-gray-500 text-sm">{{ chatTime }}</p>
        </div>
      </div>
      <div
        class="
          border-2
          rounded-xl
          py-1
          px-2
          mx-12
          whitespace-pre-line
          break-all
        "
        style="max-width: 250px"
        :class="[
          isMe
            ? 'border-blue-500 rounded-tr-none self-end'
            : 'border-orange-400 rounded-tl-none self-start',
        ]"
      >
        <span
          class="text-sm"
          style="font-family: 'Noto Sans KR'"
          v-html="chatItem.content.replace(/(?:\r\n|\r|\n)/g, '<br />')"
        ></span>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from "@vue/reactivity"
import moment from "moment"
import { onBeforeMount, onUpdated } from "@vue/runtime-core"

export default {
  name: "ChatItem",
  props: ["exChatItem", "chatItem", "myId"],
  setup(props) {
    moment.locale("ko")
    // 나 or 너
    const isMe = ref(false)
    // 새로운 날짜
    const isNewDay = ref(false)
    const newChatDate = ref(null)
    // 이어짐 or 안이어짐 ( 같은 사람 && 같은 시간(분까지) )
    const isContinue = ref(false)
    // 시간 (분 까지)
    const chatTime = ref(null)

    onBeforeMount(() => {
      // 나 or 너
      isMe.value = String(props.myId) === String(props.chatItem.sender_id)

      const chatDateTime = moment(props.chatItem.sentTime)

      // 새로운 날짜
      newChatDate.value = chatDateTime.format("LL dddd") // (년 원 일 요일)

      if (props.exChatItem) {
        const exChatDateTime = moment(props.exChatItem.sentTime)

        // 이어짐 or 안이어짐 ( 같은 사람 && 같은 시간(분까지) )
        const isSameSender =
          String(props.chatItem.sender_id) ===
          String(props.exChatItem.sender_id)
        const isSameMinute = exChatDateTime.minute() === chatDateTime.minute()
        isContinue.value = isSameSender && isSameMinute
      } else isNewDay.value = true // 새로운 날짜

      // 시간 (분 까지)
      chatTime.value = chatDateTime.format("LT")
    })

    // 새로운 날짜
    onUpdated(() => {
      if (props.exChatItem) isNewDay.value = false
      else isNewDay.value = true
    })

    return { isContinue, isMe, isNewDay, newChatDate, chatTime }
  },
}
</script>

<style lang="scss" scoped></style>
