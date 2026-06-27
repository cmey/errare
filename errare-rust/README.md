# Errare Rust

A modern Rust + WebGPU port of the legacy Errare Java game engine.

## Prerequisites

- [Rust](https://www.rust-lang.org/tools/install) (Edition 2024)
- [wasm-pack](https://rustwasm.github.io/wasm-pack/installer/) (for Web targets)

## Native Launch

To run the engine natively on Windows, macOS, or Linux:

```bash
cd errare-rust
cargo run
```

## Web/Browser Launch

The project is designed to run in modern browsers supporting WebGPU.

### 1. Build for WASM
```bash
cd errare-rust
wasm-pack build --target web
```

### 2. Run a Web Server
Since the engine loads assets via `fetch`, you must use a local web server (simple file opening won't work due to CORS). An `index.html` is provided in the root to load the generated WASM.

Using Python:
```bash
python -m http.server 8000
```
Or with Node.js:
```bash
npx serve .
```

Then visit `http://localhost:8000` in a WebGPU-enabled browser (e.g., Chrome, Edge, or Firefox Nightly).

## Development

- **Check compilation**: `cargo check`
- **Check WASM support**: `cargo check --target wasm32-unknown-unknown`
- **Linting**: `cargo clippy`

## Project Status

- [x] WebGPU Renderer (wgpu 29)
- [x] 3DS Model Loading
- [x] MD2 Animated Model Loading
- [x] Frustum Culling
- [x] Octree Spatial Partitioning
- [x] Cross-platform Asset Loading
