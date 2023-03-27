package cc.alpgo.sdtool.util;

public enum HiResUpscaler {
    NONE("None"),
    LATENT("Latent"),
    LATENT_ANTIALIASED("Latent (antialiased)"),
    LATENT_BICUBIC("Latent (bicubic)"),
    LATENT_BICUBIC_ANTIALIASED("Latent (bicubic antialiased)"),
    LATENT_NEAREST("Latent (nearist)"),
    LATENT_NEAREST_EXACT("Latent (nearist-exact)"),
    LANCZOS("Lanczos"),
    NEAREST("Nearest"),
    ESRGAN_4X("ESRGAN_4x"),
    LDSR("LDSR"),
    SCUNET_GAN("ScuNET GAN"),
    SCUNET_PSNR("ScuNET PSNR"),
    SWINIR_4X("SwinIR 4x");

    private final String value;

    HiResUpscaler(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

