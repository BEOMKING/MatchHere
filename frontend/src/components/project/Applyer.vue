<template>
  <div class="wrapper" v-if="applyerList">
    <header>
      <h3>지원자</h3>
      <span>{{ applyerList.length }}명</span>
    </header>
    <div class="member-list">
      <ApplyerItem
        v-for="applyer in applyerList"
        :key="applyer.memberId"
        :applyer="applyer"
        :projectId="projectId"
        @refuse="handleRefuse"
        @accept="handleAccept"
      />
    </div>
  </div>
</template>
<script>
import ApplyerItem from "@/components/project/ApplyerItem.vue"
import { onMounted, ref } from "vue"
import { useStore } from "vuex"

export default {
  name: "Applyer",
  components: {
    ApplyerItem,
  },
  props: {
    projectId: Number,
  },
  setup(props) {
    const store = useStore()

    const applyerList = ref()

    onMounted(async () => {
      applyerList.value = await store.dispatch(
        "project/getAllApplication",
        props.projectId
      )
      console.log(applyerList.value)
    })

    const handleAccept = async (applyer) => {
      try {
        const projectId = props.projectId
        const resData = await store.dispatch("project/acceptApplication", {
          projectId,
          applyer,
        })
        console.log(resData)
        applyerList.value = applyerList.value.filter(
          (item) => item.memberId !== applyer.memberId
        )
      } catch (error) {
        store.commit("ADD_MESSAGE", {
          text: `${error.message} 😢`,
          type: "error",
        })
      }
    }
    const handleRefuse = async (memberId) => {
      const projectId = props.projectId
      const resData = await store.dispatch("project/refuseApplication", {
        projectId,
        memberId,
      })
      console.log(resData)
      applyerList.value = applyerList.value.filter(
        (applyer) => applyer.memberId !== memberId
      )
    }

    return { applyerList, handleRefuse, handleAccept }
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
