
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
   :templates {"container" nil}}
   :cursor %cursor)
```

```clojure
(respo-composer.core/extract-templates
  (read-string (shadow.resource/inline "composer.edn")))
```

### Specs

```
Type       props              Action
----

box
space      width, height
text       value
some       value, kind
button     text               action
link       text, href         action
icon       name               action
template   name, data
list       value
input      value, textarea    action
slot       dom
inspect    title
popup      visible
case       value(?)
element    name
```

For string values,

* ones that start with `@` will be read as values
* `:x` would be read as a keyword `:x`
* `|x` would be read as a string `"x"`

for example,

* `a`, means "a"
* `@:a`, means `(get-in data [:a])`
* `@|a`, means `(get-in data ["a"])`
* `@:a :b |c`, means `(get-in data [:a :b "c"])`

data inside list children

* `index`
* `item`, list item
* `outer`, data of outer context

`some` instruction kinds:

* `nil` or `"value"`, detect with `nil?`
* `"list"`, detect with `empty?`
* `"boolean"`, detect with `#(= "false" %)`

### Workflow

Workflow https://github.com/mvc-works/calcit-workflow

### License

MIT
