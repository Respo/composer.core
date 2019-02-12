
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
`template` | `name`, `data`
`list` | `value`
`input` |  `action`, `value`, `multi-lines`

For string values,

* ones that start with `@` will be read as values
* `:x` would be read as a keyword `:x`
* `|x` would be read as a string `"x"`

for example,

* `a`, means "a"
* `@:a`, means `(get-in data [:a])`
* `@|a`, means `(get-in data ["a"])`
* `@:a :b |c`, means `(get-in data [:a :b "c"])`

### Workflow

Workflow https://github.com/mvc-works/calcit-workflow

### License

MIT
