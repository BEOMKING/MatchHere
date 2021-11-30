<template>
  <section class="container max-w-3xl py-10 grid gap-4">
    <header class="grid gap-4">
      <h2>스터디 만들기</h2>
      <p>매치히어에서 내 스터디를 만들고 나의 성공시대 시작됐다!</p>
    </header>
    <StudyForm @onSubmit="handleSubmit" />
  </section>
</template>

<script>
import StudyForm from "@/components/study/StudyForm.vue"
import { useStore } from "vuex"
import { useRouter } from "vue-router"

export default {
  name: "StudyCreate",
  components: { StudyForm },
  setup() {
    const store = useStore()
    const router = useRouter()
    const handleSubmit = async (data) => {
      const res = await store.dispatch("study/createStudy", data)
      console.log(res)
      if (res.type === "success") {
        router.push({ name: "StudyList" })
      }
    }
    return {
      handleSubmit,
    }
  },
}
</script>

<style lang="scss" scoped></style>
