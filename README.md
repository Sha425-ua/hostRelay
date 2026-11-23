# HostRelay
> 🚧 **Work in Progress**, active development.
**HostRelay** is a lightweight desktop utility for friend groups playing co-op games (Minecraft, Project Zomboid, Valheim, Terraria, etc.) without paying for a 24/7 dedicated server.
If the current host is away or at work, another friend can seamlessly pick up the latest world save from shared cloud storage, host the session, and sync changes back when done complete with host locking to prevent save conflicts and automatic modpack synchronization.

## 🗺️ Roadmap
### Phase 1: MVP
- [x] Multi-module Gradle setup (`:core` in Java 21 + `:desktop` in Compose Multiplatform)
- [ ] `StorageProvider` abstraction & `LocalStorageProvider` for local testing
- [ ] SHA-256 manifest builder & `SyncEngine` (delta upload/download + JUnit tests)
- [ ] Host lock mechanism (`state.json` + heartbeat & stale lock recovery)
- [ ] S3-compatible storage provider (Cloudflare R2 / MinIO)
- [ ] Desktop GUI: room setup (keys/nickname/game path), host status & sync controls
- [ ] Packaging & GitHub Actions CI (`.exe`/`.msi` for Windows & Linux builds)
### Phase 2: Safety, Mods & Polish
- [ ] Snapshot history & one-click world rollback
- [ ] Modpack sync (`mods/` & `config/`) with client-side ignore rules (shaders, performance mods)
- [ ] Modrinth API integration (download mods directly instead of storing them in cloud)
- [ ] Multi-game presets (Minecraft, Project Zomboid, Valheim, Terraria)
- [ ] Optional minimize-to-tray & background status monitoring (Linux Wayland & Windows)