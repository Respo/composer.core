
Composer Renderer
----

> Renderer library for Respo Composer

### Usage

```edn
[respo/composer "0.1.0-a1"]
```

```clojure
(respo-composer.core/render-markup markup
  {:data {}
   :level 1
   :templates {"container" nil}})
```

### Specs

Node type | `props`
--- | ---
`box` | -
`space` | `width`, `height`
`text` | `value`
`some` | `value`
`button` | `action`, `text`
`link` | `action`, `text`, `href`
`icon` | `action`, `name`
`template` | `name`
`list` | `value`
`input` |  `action`, `value`, `multi-lines`

### Workflow

Workflow https://github.com/mvc-works/calcit-workflow

### License

MIT
