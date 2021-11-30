<template>
  <div class="flex justify-between items-center">
    <div class="flex gap-2 items-center">
      <img :src="memberInfo.coverPicUri || profilePic" alt="" class="w-8 h-8" />
      <p class="text-gray-900 font-medium">{{ memberInfo.name }}</p>
    </div>
    <div class="flex gap-2">
      <div>
        <div
          v-if="memberInfo.authority === '소유자' || myAuth !== '소유자'"
          class="host"
        >
          {{ memberInfo.authority }}
        </div>

        <div v-else class="not-host">
          <select
            @change="handleAuthorityChange"
            class="font-bold bg-gray-200 outline-none w-full"
            v-model="currentAuthority"
          >
            <option v-for="authority in authorityList" :key="authority">
              {{ authority }}
            </option>
          </select>
        </div>
      </div>

      <div>
        <div v-if="myAuth !== '팀원'" class="role">
          <select
            @change="handleRoleChange"
            class="font-bold bg-blue-100 outline-none w-full"
            v-model="currentRole"
          >
            <option v-for="role in roleList" :key="role">{{ role }}</option>
          </select>
        </div>
        <div v-else class="role-info">
          {{ memberInfo.role }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { onMounted, ref } from "vue"
import { useStore } from "vuex"
import { useRoute, useRouter } from "vue-router"
export default {
  name: "MemberItem",
  props: ["memberInfo"],
  setup(props) {
    const profilePic = ref(require("@/assets/images/test-profile.png"))

    const roleActive = ref(false)
    const authorityActive = ref(false)
    let tempSet
    tempSet = new Set([props.memberInfo.role, "개발자", "기획자", "디자이너"])
    const roleList = ref([...tempSet])
    tempSet = new Set([props.memberInfo.authority, "소유자", "관리자", "팀원"])
    const authorityList = ref([...tempSet])

    const currentRole = ref(props.memberInfo.role)
    const currentAuthority = ref(props.memberInfo.authority)

    const myAuth = ref("")
    const route = useRoute()
    const store = useStore()
    const router = useRouter()

    onMounted(async () => {
      console.log(props.memberInfo)
      const resData = await store.dispatch(
        "project/getAuthority",
        route.params.projectId
      )
      // console.log(resData)
      myAuth.value = resData
    })

    const handleRoleChange = async (e) => {
      // console.log(e.target.value)
      const projectId = route.params.projectId
      const memberId = props.memberInfo.id
      const role = e.target.value
      try {
        const resData = await store.dispatch("project/changeRole", {
          projectId,
          memberId,
          role,
        })
        console.log(resData)
        store.commit("ADD_MESSAGE", {
          text: `${resData} 👍`,
        })
      } catch (error) {
        // console.log(currentRole.value)
        // console.log(props.memberInfo.role)
        currentRole.value = props.memberInfo.role
        // console.log(currentRole.value)

        store.commit("ADD_MESSAGE", {
          text: `${error.message} 😢`,
          type: "error",
        })
      }
    }

    const handleAuthorityChange = async (e) => {
      const projectId = route.params.projectId
      const memberId = props.memberInfo.id
      const authority = e.target.value
      try {
        const resData = await store.dispatch("project/changeAuthority", {
          projectId,
          memberId,
          authority,
        })
        console.log(resData)
        if (authority === "소유자") router.go()
      } catch (error) {
        currentAuthority.value = props.memberInfo.authority
        store.commit("ADD_MESSAGE", {
          text: `${error.message} 😢`,
          type: "error",
        })
      }
    }
    return {
      profilePic,
      roleList,
      roleActive,
      handleRoleChange,
      handleAuthorityChange,
      authorityActive,
      authorityList,
      myAuth,
      currentRole,
      currentAuthority,
    }
  },
}
</script>

<style lang="scss" scoped>
.host {
  @apply flex items-center bg-gray-200 text-gray-900 font-bold text-sm px-4 py-1 rounded-lg;
}
.not-host {
  @apply flex items-center bg-gray-200 text-gray-900 font-bold text-sm pl-2 pr-1 py-1 rounded-lg;
}

.role {
  @apply flex items-center font-bold text-sm rounded-lg  bg-blue-100 text-gray-900 pl-2 pr-1 py-1;
}

.role-info {
  @apply flex
            items-center
            bg-blue-100
            text-gray-900
            font-bold
            text-sm
            px-4
            py-1
            rounded-lg;
}
</style>
