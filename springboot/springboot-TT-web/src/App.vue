<template>
  <div class="app-shell" @pointermove="onMove">
    <div class="liquid-bg" aria-hidden="true">
      <span class="blob blob-a"></span>
      <span class="blob blob-b"></span>
      <span class="blob blob-c"></span>
      <span class="blob blob-d"></span>
      <span class="caustic"></span>
      <span class="grain"></span>
    </div>
    <div class="env-light" aria-hidden="true"></div>
    <svg class="lg-svg" aria-hidden="true">
      <filter id="lg-refract" x="-20%" y="-20%" width="140%" height="140%" color-interpolation-filters="sRGB">
        <feTurbulence type="fractalNoise" baseFrequency="0.008 0.02" numOctaves="2" seed="7" result="noise" />
        <feGaussianBlur in="noise" stdDeviation="1.2" result="soft" />
        <feDisplacementMap in="SourceGraphic" in2="soft" scale="22" xChannelSelector="R" yChannelSelector="G" />
      </filter>
    </svg>
    <router-view />
  </div>
</template>

<script setup>
const onMove = (e) => {
  document.documentElement.style.setProperty("--lg-x", `${e.clientX}px`);
  document.documentElement.style.setProperty("--lg-y", `${e.clientY}px`);
};
</script>
