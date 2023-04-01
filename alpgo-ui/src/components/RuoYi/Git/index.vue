<template>
  <div>
    <svg-icon icon-class="github" @click="goto" />
  </div>
</template>

<script>
export default {
  name: 'ALPGO-AI-Git',
  data() {
    return {
      url: 'https://github.com/ALPGO-AI/ui-admin'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>