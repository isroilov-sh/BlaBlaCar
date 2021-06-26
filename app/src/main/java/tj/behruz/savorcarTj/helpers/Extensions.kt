package tj.behruz.savorcarTj.helpers

fun String.getMonthName(month: String): String {
    var result = ""

    when(month){
        "01"->{
           result="Январь"
        }
        "02"->{
            result="Февраль"
        }
        "03"->{
            result="Март"
        }
        "04"->{
            result="Апрель"
        }
        "05"->{
            result="Май"
        }
        "06"->{
            result="Июнь"
        }
        "07"->{
            result="Июль"
        }
        "08"->{
            result="Август"
        }
        "09"->{
            result="Сентябрь"
        }
        "10"->{
            result="Октябрь"
        }
        "11"->{
            result="Ноябрь"
        }
        "12"->{
            result="Декабрь"
        }
    }
return result

}