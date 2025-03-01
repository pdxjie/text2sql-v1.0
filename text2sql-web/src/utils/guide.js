import { driver } from 'driver.js'
import 'driver.js/dist/driver.css'

export function startGuide (steps) {
    const driverInstance = driver({
        popoverClass: 'driverjs-theme',
        showProgress: false,
        allowClose: false,
        nextBtnText: '下一步',
        doneBtnText: '完成',
        prevBtnText: '上一步',
        steps: steps
    })
    driverInstance.drive()
}
