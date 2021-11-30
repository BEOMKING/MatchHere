<template>
  <div
    class="slider"
    @mousedown="handleMousedown"
    @mousemove.="handleMousemove"
    @mouseup="handleMouseup"
    @mouseleave="handleMouseleave"
  >
    <div
      class="slider-inner"
      ref="innerEl"
      @drag="handleDrag"
      style="transform: translateX(0px)"
    >
      <slot />
    </div>
  </div>
</template>

<script>
import { reactive, ref } from "@vue/reactivity"
export default {
  name: "HSlider",
  setup() {
    const innerEl = ref(null)
    const pressed = ref(false)
    const position = reactive({
      startx: 0,
      x: 0,
      sliderStartX: 0,
      sliderX: 0,
    })

    const handleOverScroll = () => {
      if (position.sliderStartX > 0) {
        position.sliderStartX = 0
        innerEl.value.classList.toggle("transition-all")
        innerEl.value.style.cssText = `transform: translateX(${position.sliderStartX}px)`
        setTimeout(() => {
          innerEl.value.classList.toggle("transition-all")
        }, 200)
      } else if (position.sliderStartX < -320 * 3) {
        position.sliderStartX = -320 * 3
        innerEl.value.classList.toggle("transition-all")
        innerEl.value.style.cssText = `transform: translateX(${position.sliderStartX}px)`
        setTimeout(() => {
          innerEl.value.classList.toggle("transition-all")
        }, 200)
      }
    }

    const handleMousedown = (e) => {
      pressed.value = true
      position.startx = e.clientX
    }
    const handleMousemove = (e) => {
      if (!pressed.value) return
      position.x = e.clientX
      position.sliderX = position.sliderStartX + position.x - position.startx
      innerEl.value.style.cssText = `transform: translateX(${position.sliderX}px)`
    }
    const handleMouseup = () => {
      pressed.value = false
      position.sliderStartX = position.sliderX
      handleOverScroll()
    }
    const handleMouseleave = () => {
      pressed.value = false
      position.sliderStartX = position.sliderX
      handleOverScroll()
    }

    return {
      innerEl,
      pressed,
      position,
      handleMousedown,
      handleMousemove,
      handleMouseup,
      handleMouseleave,
    }
  },
}
</script>

<style lang="scss" scoped>
.slider {
  @apply overflow-hidden -mx-4 px-4 py-2 cursor-pointer;

  &::-webkit-scrollbar {
    @apply hidden;
  }

  .slider-inner {
    @apply flex gap-4;
  }
}
</style>
