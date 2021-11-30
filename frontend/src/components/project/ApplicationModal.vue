<template>
  <div class="modal">
    <div class="overlay"></div>
    <div class="wrapper">
      <div class="inner">
        <section class="grid gap-4">
          <div class="header">
            <h4 class="font-bold text-lg text-gray-700">지원자 확인</h4>
            <span class="material-icons cursor-pointer" @click="$emit('close')"
              >close</span
            >
          </div>
          <hr />
        </section>
        <section class="grid gap-2 px-6">
          <div class="grid gap-4">
            <div class="flex justify-between items-center">
              <h5 class="text-gray-900 font-medium text-lg">
                {{ application.name }}
              </h5>
              <div class="flex gap-1 items-center text-sm text-gray-900">
                <span>프로필 보기</span>
                <span class="material-icons">open_in_new</span>
              </div>
            </div>
            <hr />
          </div>
          <div class="grid gap-4">
            <div class="flex justify-between items-center">
              <h6 class="font-bold text-sm text-gray-900">지원 포지션</h6>
              <span
                class="
                  bg-blue-100
                  text-gray-900
                  rounded-lg
                  py-1
                  px-4
                  font-medium
                  text-sm
                "
                >{{ application.role }}</span
              >
            </div>
            <div class="grid gap-2">
              <h6 class="font-bold text-sm text-gray-900">자기 소개</h6>
              <div
                class="border border-gray-100 p-2"
                v-html="application.bio.replace(/(?:\r\n|\r|\n)/g, '<br />')"
              ></div>
            </div>
            <div class="flex justify-center gap-4">
              <button
                @click="$emit('accept')"
                class="text-white rounded-lg py-2 px-6 bg-blue-500"
              >
                수락
              </button>
              <button
                @click="$emit('refuse')"
                class="text-white rounded-lg py-2 px-6 bg-red-400"
              >
                거절
              </button>
            </div>
          </div>
        </section>
      </div>
    </div>
  </div>
</template>

<script>
import { onMounted, ref } from "@vue/runtime-core"
import { useStore } from "vuex"
export default {
  name: "ApplicationModal",
  emits: ["close", "accept", "refuse"],
  props: {
    projectId: Number,
    memberId: Number,
  },
  setup(props) {
    const store = useStore()

    const application = ref({
      bio: "",
      email: "",
      memberId: "",
      name: "",
      projectId: "",
      role: "",
    })

    onMounted(async () => {
      application.value = await store.dispatch("project/getApplication", {
        ...props,
      })
    })

    return { application }
  },
}
</script>

<style lang="scss" scoped>
.modal {
  @apply w-screen h-screen fixed left-0 top-0 flex flex-col justify-center;
  .overlay {
    @apply w-screen h-screen fixed left-0 top-0 opacity-50 bg-black;
  }
  .wrapper {
    @apply relative;

    .inner {
      @apply m-8 shadow-xl rounded-md py-6 grid gap-6 bg-white;

      .header {
        @apply flex justify-between mx-6 items-center;
      }
    }

    .submitButton {
      @apply text-white rounded-lg px-6 py-2;
    }
  }
}
</style>
