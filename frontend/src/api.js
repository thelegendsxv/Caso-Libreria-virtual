const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8081/api'

async function request(path, options = {}) {
  const response = await fetch(API_URL + path, {
    headers: {
      'Content-Type': 'application/json',
      ...(options.headers || {})
    },
    ...options
  })

  if (!response.ok) {
    let message = 'No se pudo completar la solicitud'
    try {
      const error = await response.json()
      message = error.message || message
    } catch {}
    throw new Error(message)
  }

  if (response.status === 204) return null
  return response.json()
}

export const api = {
  getBooks: () => request('/libros'),
  getBook: (id) => request(`/libros/${id}`),
  searchSimple: (q) =>
    request(`/libros/buscar?q=${encodeURIComponent(q)}`),
  searchAdvanced: ({ autor, titulo, isbn }) => {
    const params = new URLSearchParams()
    if (autor) params.set('autor', autor)
    if (titulo) params.set('titulo', titulo)
    if (isbn) params.set('isbn', isbn)

    return request(`/libros/busqueda-avanzada?${params.toString()}`)
  },
  getAverage: (id) =>
    request(`/libros/${id}/calificaciones/promedio`),
  rateBook: (id, valor) =>
    request(`/libros/${id}/calificaciones`, {
      method: 'POST',
      body: JSON.stringify({ valor })
    }),
  getReviews: (id) =>
    request(`/libros/${id}/resenas`),
  previewReview: (id, data) =>
    request(`/libros/${id}/resenas/previsualizar`, {
      method: 'POST',
      body: JSON.stringify(data)
    }),
  saveReview: (id, data) =>
    request(`/libros/${id}/resenas`, {
      method: 'POST',
      body: JSON.stringify(data)
    })
}