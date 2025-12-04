package surveycreate.utils;

import surveycreate.models.Option;
import surveycreate.models.VoteEvent;

import java.util.Set;

public class VoteTestUtils {
    public static VoteEvent voteMockBuild(){
        return VoteEvent.builder()
                .question("¿Cuál es tu color favorito?")
                .options(Set.of(
                        Option.builder()
                                .optionId(1)
                                .description("Rojo")
                                .build(),
                        Option.builder()
                                .optionId(2)
                                .description("Azul")
                                .build(),
                        Option.builder()
                                .optionId(3)
                                .description("Verde")
                                .build(),
                        Option.builder()
                                .optionId(4)
                                .description("Amarillo")
                                .build(),
                        Option.builder()
                                .optionId(5)
                                .description("Naranja")
                                .build(),
                        Option.builder()
                                .optionId(6)
                                .description("Morado")
                                .build(),
                        Option.builder()
                                .optionId(7)
                                .description("Negro")
                                .build(),
                        Option.builder()
                                .optionId(8)
                                .description("Blanco")
                                .build(),
                        Option.builder()
                                .optionId(9)
                                .description("Rosa")
                                .build(),
                        Option.builder()
                                .optionId(10)
                                .description("Gris")
                                .build(),
                        Option.builder()
                                .optionId(11)
                                .description("Marrón")
                                .build(),
                        Option.builder()
                                .optionId(12)
                                .description("Café")
                                .build(),
                        Option.builder()
                                .optionId(13)
                                .description("Dorado")
                                .build(),
                        Option.builder()
                                .optionId(14)
                                .description("Plateado")
                                .build(),
                        Option.builder()
                                .optionId(15)
                                .description("Turquesa")
                                .build(),
                        Option.builder()
                                .optionId(16)
                                .description("Cian")
                                .build(),
                        Option.builder()
                                .optionId(17)
                                .description("Aguamarina")
                                .build(),
                        Option.builder()
                                .optionId(18)
                                .description("Lima")
                                .build(),
                        Option.builder()
                                .optionId(19)
                                .description("Uva")
                                .build(),
                        Option.builder()
                                .optionId(20)
                                .description("Púrpura")
                                .build()
                ))
                .build();
    }
}
