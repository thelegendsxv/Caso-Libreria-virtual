import { useEffect, useMemo, useState } from 'react'
import { api } from './api'

const fallbackBooks = [
  {
    id: 1,
    titulo: 'Cien años de soledad',
    autor: 'Gabriel García Márquez',
    isbn: '978-0307474728',
    precio: 45000
  },
  {
    id: 2,
    titulo: 'Don Quijote de la Mancha',
    autor: 'Miguel de Cervantes',
    isbn: '978-8424115456',
    precio: 60000
  }
]

function Stars({ value = 0, interactive = false, onSelect }) {
  return (
    <div className="stars" aria-label={`Calificación ${value} de 5`}>
      {[1, 2, 3, 4, 5].map((star) => (
        <button
          key={star}
          type="button"
          className={star <= Math.round(value) ? 'star active' : 'star'}
          disabled={!interactive}
          onClick={() => interactive && onSelect(star)}
          aria-label={`${star} estrella${star > 1 ? 's' : ''}`}
        >
          ★
        </button>
      ))}
    </div>
  )
}

function BookCard({ book, average, onOpen }) {
  return (
    <article className="book-card">
      <div className="book-cover">
        <span>LIBRERÍA</span>
        <strong>{book.titulo}</strong>
        <small>{book.autor}</small>
      </div>

      <div className="book-info">
        <p className="eyebrow">Libro</p>
        <h3>{book.titulo}</h3>
        <p className="author">{book.autor}</p>

        <div className="rating-line">
          <Stars value={average ?? 0} />
          <span>{average != null ? Number(average).toFixed(1) : 'Sin calificar'}</span>
        </div>

        <div className="card-bottom">
          <strong>${Number(book.precio || 0).toLocaleString('es-CO')}</strong>
          <button className="secondary-button" onClick={() => onOpen(book)}>
            Ver detalles
          </button>
        </div>
      </div>
    </article>
  )
}

function Modal({ book, average, reviews, onClose, onRefresh }) {
  const [rating, setRating] = useState(0)
  const [name, setName] = useState('')
  const [text, setText] = useState('')
  const [preview, setPreview] = useState(null)
  const [busy, setBusy] = useState(false)
  const [message, setMessage] = useState('')

  const submitRating = async (value) => {
    setRating(value)
    setMessage('')
    try {
      await api.rateBook(book.id, value)
      setMessage('¡Calificación registrada!')
      onRefresh()
    } catch (error) {
      setMessage(error.message)
    }
  }

  const previewReview = async () => {
    setMessage('')
    try {
      const result = await api.previewReview(book.id, {
        nombreUsuario: name,
        texto: text
      })
      setPreview(result)
    } catch (error) {
      setMessage(error.message)
    }
  }

  const publishReview = async () => {
    setBusy(true)
    setMessage('')
    try {
      await api.saveReview(book.id, {
        nombreUsuario: name,
        texto: text
      })
      setMessage('Reseña publicada correctamente.')
      setPreview(null)
      setName('')
      setText('')
      onRefresh()
    } catch (error) {
      setMessage(error.message)
    } finally {
      setBusy(false)
    }
  }

  return (
    <div className="modal-backdrop" onMouseDown={onClose}>
      <div className="modal" onMouseDown={(e) => e.stopPropagation()}>
        <button className="close-button" onClick={onClose}>×</button>

        <div className="modal-header">
          <div className="mini-cover">📖</div>
          <div>
            <p className="eyebrow">Detalles del libro</p>
            <h2>{book.titulo}</h2>
            <p className="author">{book.autor}</p>
          </div>
        </div>

        <div className="detail-grid">
          <div>
            <span>ISBN</span>
            <strong>{book.isbn}</strong>
          </div>
          <div>
            <span>Precio</span>
            <strong>${Number(book.precio || 0).toLocaleString('es-CO')}</strong>
          </div>
          <div>
            <span>Promedio</span>
            <strong>{average != null ? Number(average).toFixed(1) : 'Sin calificaciones'}</strong>
          </div>
        </div>

        <section className="modal-section">
          <h3>Califica este libro</h3>
          <Stars value={rating} interactive onSelect={submitRating} />
        </section>

        <section className="modal-section">
          <h3>Reseñas</h3>
          {reviews.length === 0 ? (
            <p className="muted">Todavía no hay reseñas.</p>
          ) : (
            <div className="reviews">
              {reviews.map((review, index) => (
                <div className="review" key={review.id ?? index}>
                  <strong>{review.nombreUsuario}</strong>
                  <p>{review.texto}</p>
                </div>
              ))}
            </div>
          )}
        </section>

        <section className="modal-section">
          <h3>Escribe una reseña</h3>
          <input
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="Tu nombre"
          />
          <textarea
            value={text}
            onChange={(e) => setText(e.target.value)}
            placeholder="¿Qué te pareció el libro?"
            rows="4"
          />

          <div className="form-actions">
            <button className="secondary-button" onClick={previewReview}>
              Previsualizar
            </button>
            <button
              className="primary-button"
              onClick={publishReview}
              disabled={busy || !name.trim() || !text.trim()}
            >
              {busy ? 'Publicando...' : 'Publicar reseña'}
            </button>
          </div>

          {preview && (
            <div className="preview">
              <small>Vista previa</small>
              <strong>{preview.nombreUsuario}</strong>
              <p>{preview.texto}</p>
            </div>
          )}

          {message && <p className="status">{message}</p>}
        </section>
      </div>
    </div>
  )
}

export default function App() {
  const [books, setBooks] = useState([])
  const [averages, setAverages] = useState({})
  const [reviews, setReviews] = useState({})
  const [selectedBook, setSelectedBook] = useState(null)
  const [search, setSearch] = useState('')
  const [advanced, setAdvanced] = useState({
    autor: '',
    titulo: '',
    isbn: ''
  })
  const [showAdvanced, setShowAdvanced] = useState(false)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [demoMode, setDemoMode] = useState(false)

  const loadBooks = async () => {
    setLoading(true)
    setError('')
    try {
      const data = await api.getBooks()
      setBooks(data)
      setDemoMode(false)
    } catch {
      setBooks(fallbackBooks)
      setDemoMode(true)
      setError('No se pudo conectar con el backend. Se muestran datos de demostración.')
    } finally {
      setLoading(false)
    }
  }

  const loadBookData = async (book) => {
    try {
      const [average, bookReviews] = await Promise.all([
        api.getAverage(book.id),
        api.getReviews(book.id)
      ])
      setAverages((prev) => ({ ...prev, [book.id]: average }))
      setReviews((prev) => ({ ...prev, [book.id]: bookReviews }))
    } catch {}
  }

  useEffect(() => {
    loadBooks()
  }, [])

  const visibleBooks = useMemo(() => books, [books])

  const doSimpleSearch = async (event) => {
    event.preventDefault()
    if (!search.trim()) {
      loadBooks()
      return
    }

    setLoading(true)
    setError('')
    try {
      setBooks(await api.searchSimple(search.trim()))
      setDemoMode(false)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const doAdvancedSearch = async (event) => {
    event.preventDefault()
    setLoading(true)
    setError('')
    try {
      setBooks(await api.searchAdvanced(advanced))
      setDemoMode(false)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const openBook = async (book) => {
    setSelectedBook(book)
    if (!demoMode) await loadBookData(book)
  }

  const refreshSelected = async () => {
    if (selectedBook && !demoMode) {
      await loadBookData(selectedBook)
    }
  }

  return (
    <div className="app">
      <header className="navbar">
        <a className="brand" href="#">
          <span className="brand-mark">L</span>
          <span>Librería <b>Virtual</b></span>
        </a>

        <nav>
          <a href="#catalogo">Catálogo</a>
          <a href="#como-funciona">Cómo funciona</a>
        </nav>

        <a className="nav-button" href="#catalogo">Explorar libros</a>
      </header>

      <main>
        <section className="hero">
          <div className="hero-copy">
            <p className="eyebrow">TU PRÓXIMA HISTORIA EMPIEZA AQUÍ</p>
            <h1>Encuentra el libro que estás buscando.</h1>
            <p className="hero-text">
              Busca por título, autor o ISBN. Explora calificaciones y comparte
              tu opinión con otros lectores.
            </p>

            <form className="search-box" onSubmit={doSimpleSearch}>
              <span>⌕</span>
              <input
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                placeholder="Buscar por título o autor..."
              />
              <button className="primary-button">Buscar</button>
            </form>

            <button
              className="advanced-toggle"
              onClick={() => setShowAdvanced((value) => !value)}
            >
              {showAdvanced ? 'Ocultar búsqueda avanzada' : 'Búsqueda avanzada →'}
            </button>
          </div>

          <div className="hero-art" aria-hidden="true">
            <div className="floating-book book-one">Historias</div>
            <div className="floating-book book-two">Ideas</div>
            <div className="floating-book book-three">Viajes</div>
            <div className="circle">✦</div>
          </div>
        </section>

        {showAdvanced && (
          <section className="advanced-panel">
            <div>
              <p className="eyebrow">BÚSQUEDA AVANZADA</p>
              <h2>Combina los criterios que necesites</h2>
            </div>

            <form onSubmit={doAdvancedSearch} className="advanced-form">
              <input
                value={advanced.titulo}
                onChange={(e) => setAdvanced({ ...advanced, titulo: e.target.value })}
                placeholder="Título"
              />
              <input
                value={advanced.autor}
                onChange={(e) => setAdvanced({ ...advanced, autor: e.target.value })}
                placeholder="Autor"
              />
              <input
                value={advanced.isbn}
                onChange={(e) => setAdvanced({ ...advanced, isbn: e.target.value })}
                placeholder="ISBN"
              />
              <button className="primary-button">Buscar</button>
            </form>
          </section>
        )}

        <section className="catalog-section" id="catalogo">
          <div className="section-heading">
            <div>
              <p className="eyebrow">CATÁLOGO</p>
              <h2>Libros para descubrir</h2>
            </div>
            <span>{visibleBooks.length} resultado{visibleBooks.length !== 1 ? 's' : ''}</span>
          </div>

          {error && <div className="notice">{error}</div>}

          {loading ? (
            <div className="empty-state">Cargando catálogo...</div>
          ) : visibleBooks.length === 0 ? (
            <div className="empty-state">
              No encontramos libros con esos criterios.
            </div>
          ) : (
            <div className="book-grid">
              {visibleBooks.map((book) => (
                <BookCard
                  key={book.id}
                  book={book}
                  average={averages[book.id]}
                  onOpen={openBook}
                />
              ))}
            </div>
          )}
        </section>

        <section className="features" id="como-funciona">
          <div>
            <p className="eyebrow">TODO EN UN SOLO LUGAR</p>
            <h2>Busca, califica y comparte.</h2>
          </div>
          <div className="feature-list">
            <article>
              <span>01</span>
              <h3>Búsqueda flexible</h3>
              <p>Encuentra libros rápidamente o combina título, autor e ISBN.</p>
            </article>
            <article>
              <span>02</span>
              <h3>Calificaciones</h3>
              <p>Valora cualquier libro de una a cinco estrellas.</p>
            </article>
            <article>
              <span>03</span>
              <h3>Reseñas</h3>
              <p>Previsualiza tu opinión antes de publicarla.</p>
            </article>
          </div>
        </section>
      </main>

      <footer>
        <span>© 2026 Librería Virtual</span>
        <span>Proyecto académico</span>
      </footer>

      {selectedBook && (
        <Modal
          book={selectedBook}
          average={averages[selectedBook.id]}
          reviews={reviews[selectedBook.id] || []}
          onClose={() => setSelectedBook(null)}
          onRefresh={refreshSelected}
        />
      )}
    </div>
  )
}