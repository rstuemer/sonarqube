define [
  'backbone'
], (
  Backbone
) ->

  class WebService extends Backbone.Model
  	idAttribute: 'path'
