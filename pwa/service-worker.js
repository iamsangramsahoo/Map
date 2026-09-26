const CACHE='palaeokids-v1';
const CORE=['/','/palaeokids'];
self.addEventListener('install', event => event.waitUntil(caches.open(CACHE).then(c => c.addAll(CORE))));
self.addEventListener('activate', event => event.waitUntil(self.clients.claim()));
self.addEventListener('fetch', event => {
  if (event.request.method !== 'GET') return;
  event.respondWith(fetch(event.request).then(resp => {
    const copy = resp.clone();
    caches.open(CACHE).then(c => c.put(event.request, copy));
    return resp;
  }).catch(() => caches.match(event.request).then(r => r || caches.match('/palaeokids'))));
});
