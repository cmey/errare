pub async fn load_binary(path: &str) -> Vec<u8> {
    #[cfg(target_arch = "wasm32")]
    {
        use wasm_bindgen::JsCast;
        use wasm_bindgen_futures::JsFuture;
        use web_sys::{Request, RequestInit, RequestMode, Response};

        let mut opts = RequestInit::new();
        opts.method("GET");
        opts.mode(RequestMode::Cors);

        let url = format!("./{}", path);
        let request = Request::new_with_str_and_init(&url, &opts).unwrap();

        let window = web_sys::window().unwrap();
        let resp_value = JsFuture::from(window.fetch_with_request(&request)).await.unwrap();
        let resp: Response = resp_value.dyn_into().unwrap();

        let data = JsFuture::from(resp.array_buffer().unwrap()).await.unwrap();
        let buffer = js_sys::Uint8Array::new(&data);
        buffer.to_vec()
    }
    #[cfg(not(target_arch = "wasm32"))]
    {
        std::fs::read(path).expect(&format!("Failed to read asset: {}", path))
    }
}
