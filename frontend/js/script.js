'use strict'

document.addEventListener('DOMContentLoaded', () => {
	// Найти элементы
	const btnUploadPhoto = document.querySelector('.btn-upload-photo')
	const firstPage = document.querySelector('.first-page')
	const secondPage = document.querySelector('.second-page')
	const uploadPhoto = document.querySelector('.upload-photo')

	// Добавить обработчик события для кнопки загрузки фото
	btnUploadPhoto.addEventListener('click', event => {
		event.preventDefault()

		// Создать элемент input для загрузки файлов
		const fileInput = document.createElement('input')
		fileInput.type = 'file'
		fileInput.accept = 'image/*' // Только изображения

		// Обработчик выбора файла
		fileInput.addEventListener('change', () => {
			const file = fileInput.files[0]
			if (file) {
				const reader = new FileReader()

				// Обработчик завершения чтения файла
				reader.onload = e => {
					// Установить загруженное изображение как фон для .upload-photo
					uploadPhoto.style.backgroundImage = `url('${e.target.result}')`
					uploadPhoto.style.backgroundSize = 'cover'
					uploadPhoto.style.backgroundPosition = 'center'

					// Скрыть первую страницу и показать вторую
					firstPage.style.display = 'none'
					secondPage.style.display = 'flex'
				}

				// Прочитать файл как Data URL
				reader.readAsDataURL(file)
			}
		})

		// Открыть диалог выбора файла
		fileInput.click()
	})
})
