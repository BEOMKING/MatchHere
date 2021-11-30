<template>
  <div class="wrapper" v-if="memberList">
    <header>
      <h3>구성원</h3>
      <span>{{ memberList.length }}명</span>
    </header>
    <div class="member-list">
      <MemberItem
        v-for="memberInfo in memberList"
        :key="memberInfo.id"
        :memberInfo="memberInfo"
      />
    </div>
  </div>
</template>

<script>
import MemberItem from "@/components/project/MemberItem.vue"
import { onMounted, ref, watch } from "@vue/runtime-core"
import { useStore } from "vuex"
export default {
  name: "Member",
  components: { MemberItem },
  props: ["projectId"],
  setup(props) {
    const store = useStore()

    const memberList = ref()

    onMounted(async () => {
      memberList.value = await store.dispatch(
        "project/getProjectMemberList",
        props.projectId
      )
      // console.log(memberList.value)
      memberList.value.forEach((member) => {
        if (member.authority === "소유자") member.authorityLevel = 2
        else if (member.authority === "관리자") member.authorityLevel = 1
        else member.authorityLevel = 0
      })
      memberList.value.sort((a, b) => {
        if (a.authorityLevel > b.authorityLevel) return -1
        else if (a.authorityLevel < b.authorityLevel) return 1
        return 0
      })
      // console.log(memberList.value)
    })

    watch(store.getters["project/getAcceptedApplyers"], () => {
      const acceptedApplyers = store.getters["project/getAcceptedApplyers"]
      memberList.value = [
        ...memberList.value,
        acceptedApplyers[acceptedApplyers.length - 1],
      ]
      // console.log(acceptedApplyers[acceptedApplyers.length - 1])
      // console.log(memberList.value)
    })

    return {
      memberList,
    }
  },
}
</script>

<style lang="scss" scoped>
.wrapper {
  @apply grid gap-4;

  header {
    @apply flex justify-between items-center;

    h3 {
      @apply font-medium text-xl text-gray-900;
    }
    span {
      @apply font-bold text-gray-900;
    }
  }
  .member-list {
    @apply grid gap-2;
  }
}
</style>
